const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const db = require('../config/database');
const util = require('util');

const queryAsync = util.promisify(db.query).bind(db);

const login = async (req, res) => {
    const { email, password } = req.body;

    if (!email || !password) {
        return res.status(400).json({ error: 'Harap isi semua kolom.' });
    }

    try {
        const query = 'SELECT * FROM user WHERE email = ?';
        const results = await queryAsync(query, [email]);

        if (results.length === 0) {
            return res.status(401).json({ error: 'Login gagal. Email tidak ditemukan.' });
        }

        const user = results[0];

        // Compare the entered password with the hashed password from the database
        const passwordMatch = await bcrypt.compare(password, user.password);

        if (passwordMatch) {
            // Passwords match, create and send a JWT token
            const token = jwt.sign(
                { userId: user.id, username: user.username, email: user.email, role: user.role },
                'secretKey',
                { expiresIn: '3h' }
            );

            res.status(200).json({
                success: true,
                message: 'Login berhasil',
                token,
                userId: user.id,
                username: user.username,
                role: user.role
            });
        } else {
            // Passwords don't match
            res.status(401).json({ error: 'Login gagal. Password salah.' });
        }
    } catch (error) {
        console.error('Kesalahan saat proses login:', error);
        res.status(500).json({ error: 'Kesalahan server.' });
    }
};





//fungsi registrasi
const register = async (req, res) => {
    const { username, email, password, confirmPassword } = req.body;
    const role = 2;

    // Validasi: Setidaknya satu angka, satu huruf, dan panjang minimal 8 karakter
    const passwordRegex = /^(?=.*\d)(?=.*[a-zA-Z]).{8,}$/;
    
    if (!username || !email || !password || !confirmPassword) {
        return res.status(400).json({ error: 'Harap isi semua kolom.' });
    }

    if (password !== confirmPassword) {
        return res.status(400).json({ error: 'Password dan konfirmasi password tidak cocok.' });
    }

    if (!passwordRegex.test(password)) {
        return res.status(400).json({ error: 'Password harus memiliki setidaknya satu angka, satu huruf, dan panjang minimal 8 karakter.' });
    }

    try {
        // Validasi email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            return res.status(400).json({ error: 'Format email tidak valid.' });
        }

        // Check if the email is already registered
        const checkEmailQuery = 'SELECT * FROM user WHERE email = ?';
        const existingUser = await queryAsync(checkEmailQuery, [email]);

        if (existingUser.length > 0) {
            return res.status(409).json({ error: 'Email sudah terdaftar.' });
        }

        // Generate salt and hash the password
        const saltRounds = 10; // Banyaknya putaran hash
        const salt = await bcrypt.genSalt(saltRounds);
        const hashedPassword = await bcrypt.hash(password, salt);

        // Insert the new user into the database
        const insertUserQuery = 'INSERT INTO user (username, email, password, role, tanggal_daftar) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)';
        await queryAsync(insertUserQuery, [username, email, hashedPassword, role]);

        res.status(201).json({ message: 'Registrasi berhasil' });
    } catch (error) {
        console.error('Kesalahan saat proses registrasi:', error);
        res.status(500).json({ error: 'Kesalahan server.' });
    }
};

module.exports = { login, register };