# SubSound

## Tech Stack & Architettura

### Badges Tecnici
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Go](https://img.shields.io/badge/Go-00ADD8?style=for-the-badge&logo=go&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![FLAC](https://img.shields.io/badge/Audio-FLAC-0055FF?style=for-the-badge)
![Subsonic API](https://img.shields.io/badge/API-Subsonic-FF8800?style=for-the-badge)

---

### Componenti di Sistema

| Layer | Tecnologia | Descrizione & Motivazione |
| :--- | :--- | :--- |
| **Front-End** | **JavaScript -> React** | Single Page Web App che sfrutta l'API HTML5 `<audio>` per lo streaming nativo di file FLAC. |
| **Back-End** | **Java (Spring Boot) / Go** | REST API compatibile con le specifiche dell'API Subsonic. Gestisce indicizzazione, metadati e HTTP Audio Streaming. |
| **Database** | **SQLite** | Database embedded relazionale e leggero per la memorizzazione di artisti, album, tracce e playlist. |
| **Storage** | **File System Locale** | Organizzazione gerarchica delle cartelle musicali su disco (es. `/musica/Artista/Album/Traccia.flac`). |

---

### Valutazione Back-End: Java (Spring Boot) vs Go

| Caratteristica | Java (Spring Boot) | Go (Golang) |
| :--- | :--- | :--- |
| **Esecuzione** | JVM Bytecode | Singolo eseguibile binario nativo |
| **Consumo RAM Base** | ~150–250 MB | ~10–20 MB |
| **Concorrenza** | Virtual Threads (Java 21+) | Goroutines & Channels nativi |
| **Streaming I/O** | Alte prestazioni, overhead di memoria maggiore | Altissime prestazioni, consumo minimo |
| **Ecosistema Audio** | Librerie mature per parsing ID3/FLAC (`jaudiotagger`) | Librerie essenziali e più frammentate |

> **Verdetto Back-End:**
> * **Go:** Ideale per massima efficienza di risorse, footprint RAM minimo e deploy immediato (perfetto per Raspberry Pi o home-server).
> * **Java:** Ideale per applicare pattern architetturali enterprise (IoC, DI) e sfruttare l'ecosistema consolidato per il parsing dei file audio.

---

### Architettura Software (3-Layer Pattern)

Il backend adotta una struttura modulare a tre livelli per garantire separazione delle responsabilità e facilità di manutenzione:

* **Controller Layer (`/rest/*`):** Espone gli endpoint REST compatibili con la specifica Subsonic, valida la richiesta e formatta le risposte JSON/XML.
* **Service Layer:** Implementa la business logic (scansione cartelle, estrazione tag FLAC, gestione della libreria musicale).
* **Repository & Data Layer:** Gestisce la persistenza dei dati tramite SQLite e si occupa dell'I/O su disco per inviare il flusso di byte audio (`HTTP Range Requests`).

---

### Roadmap di Sviluppo

- [ ] **Fase 1: Base & Echo Server**
  - [ ] Setup del progetto e struttura delle cartelle
  - [ ] Implementazione rotta ping (`/rest/ping.view`)
