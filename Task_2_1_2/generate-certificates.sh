#!/bin/bash

# Создаем хранилище ключей для сервера
keytool -genkeypair -alias serverkey -keyalg RSA -keysize 2048 \
  -storetype JKS -keystore server.jks -validity 365 \
  -storepass serverpass -keypass serverpass \
  -dname "CN=localhost, OU=NSU, O=NSU, L=Novosibirsk, S=NSK, C=RU"

# Экспортируем сертификат сервера
keytool -exportcert -alias serverkey -keystore server.jks \
  -storepass serverpass -file server.cer

# Создаем trust store для клиентов
keytool -importcert -alias serverkey -file server.cer \
  -keystore truststore.jks -storepass truststorepass \
  -noprompt