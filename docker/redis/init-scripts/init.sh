#!/bin/bash

# Ensure the Redis certificates exist
CERT_DIR="/usr/local/etc/redis"
if [ ! -f "$CERT_DIR/redis.crt" ] || [ ! -f "$CERT_DIR/redis.key" ] || [ ! -f "$CERT_DIR/redis.pem" ]; then
    echo "Generating Redis certificates..."
    openssl req -newkey rsa:2048 -nodes -keyout "$CERT_DIR/redis.key" \
        -x509 -days 365 -out "$CERT_DIR/redis.crt" -subj "/CN=redis"
    cp "$CERT_DIR/redis.crt" "$CERT_DIR/redis.pem"
fi

# Other initialization logic can go here
echo "Initialization complete."

