const express = require('express');
const router = express.Router();
const guruController = require('../controllers/guru');
const verifyTokenGuru = require('../middleware/guru');
const uploadMiddleware = require('../middleware/upload');

// Rute upload gambar dengan token verifikasi
router.post('/uploadGambar', verifyTokenGuru, uploadMiddleware.single('file'), guruController.uploadImage);

// Rute update deskripsi dengan token verifikasi
router.put('/deskripsi', verifyTokenGuru, guruController.updateDeskripsi);

module.exports = router;