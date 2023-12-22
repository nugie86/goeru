const jwt = require('jsonwebtoken');

const authMiddleware = (req, res, next) => {
  // Mendapatkan token dari header Authorization
  const authHeader = req.header('Authorization');

  // Memeriksa apakah token ada
  if (!authHeader) {
    return res.status(401).json({ error: 'Unauthorized - Token not provided' });
  }

  // Mengekstrak token dari header
  const tokenParts = authHeader.split(' ');
  const token = tokenParts[1];

  try {
    // Verifikasi token
    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    // Menyimpan userId dan role dari token ke dalam request
    req.user = {
      userId: decoded.userId,
      role: decoded.role,
    };

    // Lanjut ke middleware atau rute berikutnya
    next();
  } catch (error) {
    console.error('Error verifying token:', error);
    return res.status(401).json({ error: 'Unauthorized - Invalid token' });
  }
};

module.exports = authMiddleware;

