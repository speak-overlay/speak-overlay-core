#!/bin/sh
set -e

echo "⏳ Esperando a que MinIO esté disponible..."
until mc alias set myminio $MINIO_SERVER $MINIO_ROOT_USER $MINIO_ROOT_PASSWORD 2>/dev/null; do
  sleep 2
done

echo "✅ MinIO disponible, creando usuario y bucket..."

# Generar claves aleatorias sin openssl (usa /dev/urandom)
ACCESS_KEY=$(head /dev/urandom | tr -dc A-Z0-9 | head -c 15)
SECRET_KEY=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 40)

echo "🔐 Credenciales generadas:"
echo "  Access Key: $ACCESS_KEY"
echo "  Secret Key: $SECRET_KEY"

# Crear usuario
mc admin user add myminio "$ACCESS_KEY" "$SECRET_KEY" || true

# Asignar política (nuevo formato)
mc admin policy attach myminio readwrite --user "$ACCESS_KEY" || true

# Crear bucket si no existe
mc mb myminio/"$MINIO_BUCKET" || true

# Guardar las credenciales en un archivo dentro del volumen del contenedor minio
mkdir -p /config
echo "ACCESS_KEY=$ACCESS_KEY" > /config/credentials.env
echo "SECRET_KEY=$SECRET_KEY" >> /config/credentials.env
echo "BUCKET=$MINIO_BUCKET" >> /config/credentials.env

echo "🎉 Usuario y bucket creados exitosamente:"
cat /config/credentials.env
