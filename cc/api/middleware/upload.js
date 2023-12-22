// middleware/guru.js
const multer = require('multer');

const storage = multer.memoryStorage();
const upload = multer({
  storage: storage,
  fileFilter: function (req, file, cb) {
    const allowedFileTypes = /jpeg|jpg|png/;
    const extname = allowedFileTypes.test(file.originalname.toLowerCase());

    if (extname) {
      return cb(null, true);
    } else {
      cb('Error: File type not allowed (only jpeg, jpg, and png)');
    }
  },
});

module.exports = upload.single('file');
