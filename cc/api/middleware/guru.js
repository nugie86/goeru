const jwt = require('jsonwebtoken');

const hanyaGuru = (req, res, next) => {
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

    // Menyimpan userId dari token ke dalam request
    req.userId = decoded.userId;

    // Menyimpan role dari token ke dalam request
    req.role = decoded.role;

    // Memeriksa role
    if (req.role !== 1) {
      return res.status(403).json({ error: 'Forbidden - Akses ditolak. Hanya role 1 yang diizinkan.' });
    }

    // Lanjut ke middleware atau rute berikutnya

    req.user = {
      userId: decoded.userId,
      role: decoded.role,
    };

    next();
  } catch (error) {
    console.error('Error verifying token:', error);
    return res.status(401).json({ error: 'Unauthorized - Invalid token' });
  }
};

module.exports = hanyaGuru;
