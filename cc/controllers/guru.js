// controllers/guru.js
const db = require('../config/database');
const util = require('util');
const query = util.promisify(db.query).bind(db);
const { Storage } = require('@google-cloud/storage');

const storage = new Storage({
  keyFilename: './config/keyfile.json',
});
const bucketName = 'coba-goeru';
const bucket = storage.bucket(bucketName);

const uploadGambar = async (req, res) => {
  try {
    const { userId } = req.user;

    // Validasi apakah req.file tidak kosong
    if (!req.file) {
      return res.status(400).json({ status: 'error', message: 'Tidak ada file yang diunggah.' });
    }

    // Menghapus gambar sebelumnya jika ada
    const guruData = await query(`SELECT gambar FROM guru WHERE id_user = ?`, [userId]);

    if (guruData && guruData.length > 0 && guruData[0].gambar) {
      const urlGambar = guruData[0].gambar;

      // Hapus gambar sebelumnya dari Google Cloud Storage
      const decodedUrl = decodeURIComponent(urlGambar); // Dekode URL jika perlu
      const objekGambar = bucket.file(decodedUrl.replace(`https://storage.googleapis.com/${bucketName}/`, ''));

      objekGambar
        .delete()
        .then(() => {
          // Handle success
          console.log('Previous image deleted successfully');
        })
        .catch((error) => {
          // Handle error
          console.error('Error deleting previous image:', error);
          // Memberikan respons 500 ke klien atau penanganan kesalahan lainnya
          res.status(500).json({ status: 'error', message: 'Error deleting previous image' });
        });
    }

    // Upload gambar baru ke Google Cloud Storage
    const gambar = req.file;
    const timestamp = Date.now(); // Waktu saat ini dalam milidetik
    const namaFile = `${timestamp}_${gambar.originalname}`; // Nama file dengan timestamp
    const blob = bucket.file(namaFile);
    const blobStream = blob.createWriteStream();

    blobStream.on('finish', async () => {
      // Mendapatkan URL gambar di Google Cloud Storage
      const urlGambar = `https://storage.googleapis.com/${bucketName}/${namaFile}`;

      // Update URL gambar di MySQL
      await query(`UPDATE guru SET gambar = ? WHERE id_user = ?`, [urlGambar, userId]);

      res.status(200).json({ status: 'success', url_gambar: urlGambar });
    });

    blobStream.end(gambar.buffer);
  } catch (error) {
    console.error(error);
    res.status(500).json({ status: 'error', message: error.message });
  }
};




const updateDeskripsi = async (req, res) => {
  try {
    const { userId } = req.user;
    const { deskripsi } = req.body;

    if (!deskripsi) {
      return res.status(400).json({ error: 'Deskripsi cannot be empty' });
    }

    const updateQuery = 'UPDATE guru SET deskripsi = ? WHERE id_user = ?';
    await query(updateQuery, [deskripsi, userId]);

    res.json({ message: 'Deskripsi updated successfully' });
  } catch (error) {
    console.error('Error updating deskripsi:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
};




module.exports = { uploadGambar, updateDeskripsi };