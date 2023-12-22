// controllers/user.js

const db = require('../config/database');
const util = require('util');
const query = util.promisify(db.query).bind(db);

// Helper function to get ratings by guru ID
const getRatingsByIdGuru = async (idGuru) => {
  const ratingQuery = 'SELECT * FROM rating WHERE id_guru = ?';
  const ratings = await query(ratingQuery, [idGuru]);

  return ratings.map((rating) => ({
    username_pemberi_rating: rating.id_user,
    tanggal_rating: rating.tanggal_rating,
    nilai: rating.nilai,
    komentar: rating.komentar,
  }));
};

// Helper function to get five gurus with similar subjects
const getSimilarGurus = async (mapel, limit = 5) => {
  const similarGurusQuery = 'SELECT * FROM guru WHERE mapel = ? LIMIT ?';
  const similarGurus = await query(similarGurusQuery, [mapel, limit]);

  return similarGurus.map((guru) => ({
    id_user: guru.id_user,
    gambar: guru.gambar,
    nama: guru.nama,
    mapel: guru.mapel,
    asal: guru.asal,
    harga: guru.harga,
  }));
};

// Endpoint: GET /user/v1/allGuru
const getAllGuru = async (req, res) => {
  try {
    const results = await query('SELECT * FROM guru');

    // Pastikan result adalah array sebelum menggunakan map
    const guruList = Array.isArray(results)
      ? results.map((guru) => ({
          id_user: guru.id_user,
          gambar: guru.gambar,
          nama: guru.nama,
          mapel: guru.mapel,
          asal: guru.asal,
          harga: guru.harga,
        }))
      : [];

    res.json({ guru: guruList });
  } catch (error) {
    console.error('Error fetching guru data:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Endpoint: GET /user/v1/guru/:idGuru
const getGuruById = async (req, res) => {
  try {
    const { idGuru } = req.params;
    const guruQuery = 'SELECT * FROM guru WHERE id_user = ?';
    const guruResult = await query(guruQuery, [idGuru]);

    if (guruResult.length === 0) {
      return res.status(404).json({ error: 'Guru not found' });
    }

    const guruData = {
      id_user: guruResult[0].id_user,
      gambar: guruResult[0].gambar,
      nama: guruResult[0].nama,
      mapel: guruResult[0].mapel,
      asal: guruResult[0].asal,
      harga: guruResult[0].harga,
      deskripsi: guruResult[0].deskripsi,
    };

    const ratings = await getRatingsByIdGuru(idGuru);
    guruData.ratings = ratings;

    const similarGurus = await getSimilarGurus(guruData.mapel);
    guruData.similarGurus = similarGurus;

    res.json({ guru: guruData });
  } catch (error) {
    console.error('Error fetching guru data:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
};



const saveOrUpdatePreferensi = async (req, res) => {
  try {
    const { userId } = req.user;
    const {
      usia,
      guru_favorit_Mathematics,
      guru_favorit_Physics,
      guru_favorit_Biology,
      guru_favorit_Chemistry,
      guru_favorit_Economy,
      guru_favorit_Sosiology,
      guru_favorit_Geography,
      guru_favorit_History,
      guru_favorit_Antropology,
    } = req.body;

    
    if (
      !usia ||
      !guru_favorit_Mathematics ||
      !guru_favorit_Physics ||
      !guru_favorit_Biology ||
      !guru_favorit_Chemistry ||
      !guru_favorit_Economy ||
      !guru_favorit_Sosiology ||
      !guru_favorit_Geography ||
      !guru_favorit_History ||
      !guru_favorit_Antropology
    ) {
      return res.status(400).json({ success: false, error: 'All fields must be filled' });
    }

    

    const query = `
      INSERT INTO preferensi 
      (id_user, usia, guru_favorit_Mathematics, guru_favorit_Physics, guru_favorit_Biology, guru_favorit_Chemistry, guru_favorit_Economy, guru_favorit_Sosiology, guru_favorit_Geography, guru_favorit_History, guru_favorit_Antropology) 
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
      ON DUPLICATE KEY UPDATE
      usia = VALUES(usia),
      guru_favorit_Mathematics = VALUES(guru_favorit_Mathematics),
      guru_favorit_Physics = VALUES(guru_favorit_Physics),
      guru_favorit_Biology = VALUES(guru_favorit_Biology),
      guru_favorit_Chemistry = VALUES(guru_favorit_Chemistry),
      guru_favorit_Economy = VALUES(guru_favorit_Economy),
      guru_favorit_Sosiology = VALUES(guru_favorit_Sosiology),
      guru_favorit_Geography = VALUES(guru_favorit_Geography),
      guru_favorit_History = VALUES(guru_favorit_History),
      guru_favorit_Antropology = VALUES(guru_favorit_Antropology)
    `;
    
    await db.query(query, [
      userId,
      usia,
      guru_favorit_Mathematics,
      guru_favorit_Physics,
      guru_favorit_Biology,
      guru_favorit_Chemistry,
      guru_favorit_Economy,
      guru_favorit_Sosiology,
      guru_favorit_Geography,
      guru_favorit_History,
      guru_favorit_Antropology,
    ]);

    res.json({ success: true, message: 'Preferensi guru favorit berhasil disimpan' });
  } catch (error) {
    console.error('Error saving preferensi guru:', error);
    res.status(500).json({ success: false, error: 'Internal Server Error' });
  }
};

const addRating = async (req, res) => {
  const { userId } = req.user;
  const { guruId, nilai, komentar } = req.body;

  try {
    // Check if guruId and nilai are provided
    if (!guruId || !nilai) {
      return res.status(400).json({ error: 'Guru ID dan nilai harus diisi.' });
    }

    // Check if nilai is within the range of 0 to 5
    if (nilai < 0 || nilai > 5) {
      return res.status(400).json({ error: 'Nilai harus berada dalam rentang dari 0 hingga 5.' });
    }

    // Insert rating into the database
    const insertQuery = 'INSERT INTO rating (id_user, id_guru, nilai, komentar, tanggal_rating) VALUES (?, ?, ?, ?, ?)';
    const currentDate = new Date().toISOString().slice(0, 10);

    await new Promise((resolve, reject) => {
      db.query(insertQuery, [userId, guruId, nilai, komentar, currentDate], (err, result) => {
        if (err) {
          console.error('Error inserting rating:', err);
          reject('Internal Server Error');
        } else {
          resolve(result);
        }
      });
    });

    res.status(201).json({
      success: true,
      message: 'Rating added successfully',
    });
  } catch (error) {
    console.error('Error adding rating:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
};


module.exports = { getAllGuru, getGuruById, saveOrUpdatePreferensi, addRating };
