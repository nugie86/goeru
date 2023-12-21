const jwt = require('jsonwebtoken');

const verifyTokenUser = (req, res, next) => {
    const authHeader= req.header('Authorization');
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
        return res.status(401).json({ error: 'Token tidak ditemukan. Akses ditolak.' });
    }

    try {
        const decoded = jwt.verify(token, 'secretKey');
        req.user = decoded;
        next();
    } catch (error) {
        console.error('Kesalahan verifikasi token:', error);
        res.status(401).json({ error: 'Token tidak valid. Akses ditolak.' });
    }
};

module.exports = verifyTokenUser;
