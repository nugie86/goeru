const connection = require('../config/database');

const getAllGuru = (req, res) => {
    // Query SQL untuk mengambil semua data guru
    const sql = 'SELECT id_user, gambar, nama, mapel, asal, harga FROM guru';

    connection.query(sql, (error, results, fields) => {
        if (error) {
            console.error('Error executing SQL query:', error);
            res.status(500).json({ error: 'Internal Server Error during database query' });
            return;
        }

        res.json({ guru: results });
    });
};



const getGuruById = (req, res) => {
    const guruId = req.params.id;

    // Query SQL untuk mengambil data guru, rating, dan username pemberi rating berdasarkan ID guru
    const sql = `
        SELECT guru.*, rating.nilai, rating.komentar, DATE_FORMAT(rating.tanggal_rating, '%d-%m-%Y') as tanggal_rating, user.username as username_pemberi_rating
        FROM guru
        LEFT JOIN rating ON guru.id_user = rating.id_guru
        LEFT JOIN user ON rating.id_user = user.id
        WHERE guru.id_user = ?
    `;

    connection.query(sql, [guruId], (error, results, fields) => {
        if (error) {
            console.error('Error executing SQL query:', error);
            res.status(500).json({ error: 'Internal Server Error during database query' });
            return;
        }

        if (results.length === 0) {
            res.status(404).json({ error: 'Guru not found' });
        } else {
            // Menggabungkan data guru hanya sekali dan data rating menjadi array objek
            const guruData = {
                id_user: results[0].id_user,
                gambar: results[0].gambar,
                nama: results[0].nama,
                mapel: results[0].mapel,
                asal: results[0].asal,
                harga: results[0].harga,
                deskripsi: results[0].deskripsi,
                // Menambahkan data rating jika ada
                ratings: results.map(result => ({
                    username_pemberi_rating: result.username_pemberi_rating,
                    tanggal_rating: result.tanggal_rating,
                    nilai: result.nilai,
                    komentar: result.komentar
                }))
            };

            res.json({ guru: guruData });
        }
    });
};

const postRating = (req, res) => {
    const { userId } = req.user;
    const { guruId, nilai, komentar } = req.body;


    if (!guruId || !nilai) {
        return res.status(400).json({ error: 'Guru ID dan nilai harus diisi.' });
    }

    // Validasi nilai
    const numericNilai = parseFloat(nilai.replace(',', '.'));
    if (isNaN(numericNilai) || numericNilai < 0 || numericNilai > 5) {
        return res.status(400).json({ error: 'Nilai harus berada dalam rentang dari 0 hingga 5.' });
    }

    // Query SQL untuk menambahkan rating
    const sql = `
      INSERT INTO rating (id_guru, id_user, nilai, komentar, tanggal_rating)
      VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)
    `;

    connection.query(sql, [guruId, userId, numericNilai, komentar], (error, results, fields) => {
        if (error) {
            console.error('Error executing SQL query:', error);
            res.status(500).json({ error: 'Internal Server Error during database query' });
            return;
        }

        res.json({ success: true, message: 'Rating added successfully' });
    });
};

const postPreferensi = (req, res) => {
    const { userId } = req.user;
    const { usia, guru_favorit } = req.body;
    
    if (!userId || !usia || !guru_favorit) {
        return res.status(400).json({ error: 'Semua bidang harus diisi' });
    }

    const sql = `
        INSERT INTO preferensi (id_user, usia, guru_favorit)
        VALUES (?, ?, ?)
        ON DUPLICATE KEY UPDATE usia = VALUES(usia), guru_favorit = VALUES(guru_favorit)
    `;

    connection.query(sql, [userId, usia, guru_favorit], (error, results, fields) => {
        if (error) {
            console.error('Error executing SQL query:', error);
            res.status(500).json({ error: 'Internal Server Error during database query' });
            return;
        }

        res.json({ message: 'Preferensi berhasil disimpan' });
    });
};





module.exports = {
    getAllGuru,
    getGuruById,
    postRating,
    postPreferensi,
};