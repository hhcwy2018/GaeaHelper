"C:\Program Files\Git\usr\bin\openssl" genpkey -aes-256-cbc -algorithm RSA -out private_encrypted.pem -pkeyopt rsa_keygen_bits:4096

"C:\Program Files\Git\usr\bin\openssl" rsa  -in private_encrypted.pem  -out private.pem

"C:\Program Files\Git\usr\bin\openssl" req  -key private.pem  -new  -x509  -days 365  -out chain.crt