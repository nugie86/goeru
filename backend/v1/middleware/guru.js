const jwt = require('jsonwebtoken');

const verifyTokenRole1 = (req, res, next) => {
    const authHeader = req.header('Authorization');
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
        return res.status(401).json({ error: 'Token tidak ditemukan. Akses ditolak.' });
    }

    try {
        const decoded = jwt.verify(token, 'secretKey');

        if (decoded.role === 1) {
            req.user = decoded;
            next();
        } else {
            return res.status(403).json({ error: 'Akses ditolak. Hanya role 1 yang diizinkan.' });
        }
    } catch (error) {
        console.error('Kesalahan verifikasi token:', error);
        res.status(401).json({ error: 'Token tidak valid. Akses ditolak.' });
    }
};

module.exports = verifyTokenRole1;
