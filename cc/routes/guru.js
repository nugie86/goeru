const express = require('express');
const router = express.Router();

const guruControllers = require('../controllers/guru');
const hanyaGuru = require('../middleware/guru');
const singleFile = require('../middleware/upload');

router.put('/deskripsi', hanyaGuru, guruControllers.updateDeskripsi);
router.post('/uploadGambar', hanyaGuru, singleFile, guruControllers.uploadGambar);


module.exports = router;