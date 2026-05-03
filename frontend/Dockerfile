# =====================================================================
# Dockerfile per il frontend Next.js — Multi-stage build
# =====================================================================
#
# CONCETTO: Multi-stage build (3 stage)
#
# Uguale al Dockerfile del backend, ma con 3 stage invece di 2:
#
#   Backend (2 stage):
#     Stage 1 (build): Maven + codice → compila → crea .jar
#     Stage 2 (run):   JDK leggero + .jar → esegue
#
#   Frontend (3 stage):
#     Stage 1 (deps):  Node.js + package.json → installa dipendenze
#     Stage 2 (build): Node.js + codice + dipendenze → builda → crea standalone
#     Stage 3 (run):   Node.js minimale + standalone → esegue
#
# Perché 3 stage? Per CACHE ottimale:
# - Le dipendenze (npm install) cambiano raramente
# - Il codice (src/) cambia spesso
# - Separandoli, Docker riesegue solo lo stage che cambia
# =====================================================================

# STAGE 1: Installa le dipendenze
# --------------------------------
FROM node:22-alpine AS deps

WORKDIR /app

# Copia SOLO i file di dipendenze (non il codice sorgente)
# Se questi file non cambiano, Docker usa la cache → build veloce
COPY package.json package-lock.json ./

# Installa le dipendenze
# --frozen-lockfile = usa esattamente le versioni nel lockfile (riproducibilità)
RUN npm ci --frozen-lockfile


# STAGE 2: Build dell'applicazione
# --------------------------------
FROM node:22-alpine AS builder

WORKDIR /app

# Copia le dipendenze dallo stage precedente
COPY --from=deps /app/node_modules ./node_modules

# Copia tutto il codice sorgente
COPY . .

# VARIABILE D'AMBIENTE PER IL BUILD:
# Durante il build, Next.js "brucia" le variabili NEXT_PUBLIC_* nell'HTML/JS.
# In Docker, il frontend non chiama localhost:8080 ma il nome del servizio "app".
# Però le variabili NEXT_PUBLIC_* sono per il BROWSER — il browser non è dentro Docker!
#
# SOLUZIONE: usiamo una variabile server-side (senza NEXT_PUBLIC_) per il fetch
# fatto dal Server Component, e lasciamo la NEXT_PUBLIC_ per il fetch del browser.
#
# Per il momento settiamo un placeholder che verrà sovrascritto a runtime.
ENV NEXT_PUBLIC_API_URL=http://localhost:8080

# Esegui il build di Next.js in modalità standalone
RUN npm run build


# STAGE 3: Esecuzione (immagine finale leggera)
# -----------------------------------------------
FROM node:22-alpine AS runner

WORKDIR /app

# Imposta ambiente di produzione
ENV NODE_ENV=production

# Copia SOLO i file necessari dal build:
# 1. Il bundle standalone (contiene server.js + mini node_modules)
COPY --from=builder /app/.next/standalone ./

# 2. I file statici (CSS, JS, immagini)
COPY --from=builder /app/.next/static ./.next/static

# 3. La cartella public (favicon, immagini)
COPY --from=builder /app/public ./public

# Esponi la porta 3000 (default di Next.js)
EXPOSE 3000

# Avvia il server Next.js standalone
# Il server.js è generato automaticamente da "output: standalone"
CMD ["node", "server.js"]
