const { Storage } = require('@google-cloud/storage');
const connection = require('../config/database');
const util = require('util');
const mime = require('mime-types');

const storage = new Storage({
    projectId: 'percobaan-407709',
    keyFilename: './config/keyfile.json'
});

const bucketName = 'coba-goeru';
const query = util.promisify(connection.query).bind(connection);

exports.uploadImage = async (req, res) => {
    try {
        const userId = req.user.userId;
        const file = req.file;

        if (!file) {
            return res.status(400).send('Tidak ada file yang diunggah.');
        }

        // Pengecekan tipe konten
        const allowedMimeTypes = ['image/jpeg', 'image/jpg', 'image/png'];
        const contentType = mime.lookup(file.originalname);

        if (!allowedMimeTypes.includes(contentType)) {
            return res.status(400).send('Format file tidak didukung. Harap unggah gambar dengan format JPG, JPEG, atau PNG.');
        }

        const filename = Date.now() + file.originalname;
        const fileBuffer = file.buffer;

        // Unggah file ke Google Cloud Storage
        await storage.bucket(bucketName).file(filename).save(fileBuffer);

        const imageUrl = `https://storage.googleapis.com/${bucketName}/${filename}`;

        // Gunakan pendekatan berbasis promise untuk kueri database
        const selectResults = await query('SELECT gambar FROM guru WHERE id_user = ?', [userId]);

        // Jika ada URL gambar yang sudah ada, hapus file sebelumnya dari Google Cloud Storage
        if (selectResults.length > 0 && selectResults[0].gambar) {
            const existingImageUrl = selectResults[0].gambar;
            const existingFilename = existingImageUrl.split('/').pop();
            await storage.bucket(bucketName).file(existingFilename).delete();
        }

        // Masukkan atau perbarui URL gambar di tabel guru untuk pengguna tertentu
        await query('INSERT INTO guru (id_user, gambar) VALUES (?, ?) ON DUPLICATE KEY UPDATE gambar = VALUES(gambar)', [userId, imageUrl]);

        res.status(200).send('Gambar berhasil diunggah dan disimpan.');
    } catch (error) {
        console.error(error);
        res.status(500).send('Terjadi kesalahan.');
    }
};


exports.updateDeskripsi = (req, res) => {
    const { userId } = req.user; // Menggunakan userId dari decode token
    const { deskripsi } = req.body;

    if (!deskripsi) {
        return res.status(400).json({ error: 'Deskripsi cannot be empty' });
    }

    const query = 'UPDATE guru SET deskripsi = ? WHERE id_user = ?';

    connection.query(query, [deskripsi, userId], (err, result) => {
        if (err) {
            console.error('Error updating deskripsi: ' + err.stack);

            // Handle specific MySQL error codes
            if (err.code === 'ER_DUP_ENTRY') {
                return res.status(409).json({ error: 'Deskripsi already exists. Please provide a different one.' });
            }

            return res.status(500).json({ error: 'Internal Server Error' });
        } else {
            if (result.affectedRows === 0) {
                return res.status(404).json({ error: 'User not found or no changes made' });
            }

            res.status(200).json({ message: 'Deskripsi updated successfully' });
        }
    });
};