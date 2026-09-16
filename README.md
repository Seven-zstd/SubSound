# SubSound

Un server musicale self-hosted, leggero e compatibile con le specifiche **Subsonic API**, progettato per lo streaming di alta qualità (con focus su formati lossless) e la gestione intelligente della libreria musicale.

* * *

## Tech Stack & Architettura

### Badges Tecnici
![Java21](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![Audio Formats](https://img.shields.io/badge/Audio-Lossless_%28FLAC/WAV%29_&_Standard_%28MP3/OGG%29-0055FF?style=for-the-badge) 
![API](https://img.shields.io/badge/API-Subsonic_Compatible-FF8800?style=for-the-badge)


* * *

### Componenti di Sistema

| Layer | Tecnologia | Descrizione & Motivazione |
| :--- | :--- | :--- |
| **Front-End** | ****JavaScript -> React**** | Single Page Application (SPA) che sfrutta l'API HTML5 `<audio>` per lo streaming nativo e la gestione dei metadati. |
| **Back-End** | **Java (Spring Boot)** | REST API compatibile con le specifiche Subsonic. Gestisce indicizzazione, parsing metadati e HTTP Audio Streaming. |
| **Database** | **SQLite** | Database embedded relazionale e leggero per la memorizzazione di artisti, album, tracce e playlist. |
| **Storage** | **File System Locale** | Organizzazione gerarchica standard delle cartelle musicali, agnostica rispetto all'estensione del file. |

* * *

### Configurazione Spring Boot

- **Setup:** `Java 21` | `Maven` | `Spring Boot 3.x` | `Packaging: Jar` | `Config: YAML`
- **Base Package:** `com.subsound.server`

**Dipendenze Progetto:**

- [x] **Spring Web:** Framework MVC per API REST con Apache Tomcat integrato.
- [x] **Lombok:** Riduzione del codice boilerplate (getter, setter, builder).
- [ ] **SQLite:** Database relazionale embedded.
- [ ] **FFmpeg (Process Wrapper):** Per transcoding on-the-fly e conversione tra formati (es. da lossless a lossy per client mobili).
- [ ] **JAudioTagger:** Libreria matura per il parsing dei metadati audio (FLAC, ID3, Vorbis, APE).

> **Aggiungere dipendenze successive:**  
> Per integrare nuove librerie in seguito, inserisci il relativo blocco all'interno della sezione `<dependencies>` del file `pom.xml`:
> 
> ```xml
> <dependency>
>   <groupId>org.xerial</groupId>
>   <artifactId>sqlite-jdbc</artifactId>
> </dependency>
> ```

* * *

### Valutazione Architetturale: Java (Spring Boot) vs Go

| Caratteristica | Java (Spring Boot) | Go (Golang) |
| :--- | :--- | :--- |
| **Esecuzione** | JVM Bytecode (JIT) | Singolo eseguibile binario nativo |
| **Consumo RAM Base** | ~150–250 MB | ~10–20 MB |
| **Concorrenza** | Virtual Threads (Java 21+) | Goroutines & Channels nativi |
| **Streaming I/O** | Alte prestazioni, leggero overhead di memoria | Altissime prestazioni, consumo minimo |
| **Ecosistema Audio** | Librerie mature e stabili per parsing (`jaudiotagger`) | Librerie più essenziali e talvolta frammentate |

**Verdetto:**

- **Go** è ideale per la massima efficienza di risorse e deploy immediato (perfetto per Raspberry Pi).
- **Java** è stato scelto per applicare pattern architetturali enterprise (IoC, DI), garantire robustezza nel parsing di file audio complessi (specialmente lossless) e sfruttare un ecosistema di librerie consolidato.

* * *

### Architettura Software (3-Layer Pattern)

Il backend adotta una struttura modulare a tre livelli per garantire separazione delle responsabilità e facilità di manutenzione:

1.  **Controller Layer** (`/rest/*`): Espone gli endpoint REST compatibili con la specifica Subsonic, valida le richieste e formatta le risposte (JSON/XML).
2.  **Service Layer**: Implementa la business logic (scansione cartelle, estrazione tag, logica di normalizzazione, gestione libreria).
3.  **Repository & Data Layer**: Gestisce la persistenza su SQLite e l'I/O su disco per l'invio del flusso di byte audio (supporto a HTTP Range Requests).

* * *

### Funzionalità Core (Feature Set)

| ID  | Feature | Descrizione Dettagliata | Priorità | Stato |
| :---: | :--- | :--- | :---: | :---: |
| 1   | **Ping / Health-check** | Endpoint base `/rest/ping.view` per verificare la raggiungibilità del server. | P0  | 🚧 Fase 1 |
| 2   | **Autenticazione Standard Subsonic** | Implementazione del protocollo di login con supporto a token, salt e timestamp (`u`, `p`, `s`, `t`) per garantire la compatibilità con qualsiasi client open-source Subsonic compatibile. | P0  | ⬜ Fase 2 |
| 3   | **Scansione Libreria** | Scansione ricorsiva di cartelle musicali con supporto nativo a formati lossless (FLAC, WAV, ALAC) e standard (MP3, OGG). | P0  | ⬜ Fase 2 |
| 4   | **Estrazione Metadati** | Parsing robusto di tag audio (FLAC, ID3, Vorbis) via `jaudiotagger` per titolo, artista, album, anno e numero di traccia. | P0  | ⬜ Fase 2 |
| 5   | **Controllo Hash** | Controllo hash (es. SHA-256) a livello di database per identificare possibili dupplicati e avvisare nell'apposita sezione. | P1  | ⬜ Fase 3 |
| 6   | **Streaming Audio** | Supporto a HTTP Range Requests (`Accept-Ranges`, `Content-Range`) per consentire il seek rapido e lo streaming efficiente di file di grandi dimensioni. | P0  | ⬜ Fase 3 |
| 7   | **Navigazione Libreria** | Endpoint per la navigazione gerarchica: Artisti → Album → Tracce (`getArtists`, `getAlbumList2`, `getSongs`). | P1  | ⬜ Fase 3 |
| 8   | **Testi Sincronizzati** | Supporto e modifica di file `.lrc` per la visualizzazione dei testi a tempo, sia lato server che client. | P1  | ⬜ Fase 4 |
| 9   | **Ricerca Full-Text** | Ricerca unificata (`/rest/search.view`) su artista, album e titolo della traccia. | P1  | ⬜ Fase 4 |
| 10  | **Transcoding & Download** | Conversione on-the-fly e download in formati alternativi (es. FLAC → MP3/AAC) tramite wrapper FFmpeg, per risparmiare banda su client mobili. | P2  | ⬜ Fase 5 |
| 11  | **Normalizzazione Volume** | Supporto e generazione di metadati ReplayGain (modalità Automatica, Album, Brano) per un volume di riproduzione uniforme. | P2  | ⬜ Fase 5 |
| 12  | **Gestione Playlist** | CRUD completo per playlist (`createPlaylist`, `updatePlaylist`, `getPlaylists`). | P2  | ⬜ Fase 5 |
| 13  | **Condivisione Link** | Generazione di link pubblici per brani o playlist (funzionante solo se la condivisione tramite URI pubblico è impostata). | P2  | ⬜ Fase 6 |
| 14  | **Riproduzione Sincronizzata** | "Party Mode": riproduzione contemporanea e sincronizzata a tempo tra due o più utenti connessi. | P3  | ⬜ Futuro |
| 15  | **Smart Grouping & Plugin** | Raggruppamento intelligente dei generi musicali e architettura predisposta per il supporto di plugin esterni. | P3  | ⬜ Futuro |

* * *

### Roadmap di Sviluppo

- [ ] **Fase 1: Base & Echo Server**
    - [ ] Setup del progetto e struttura delle cartelle
    - [ ] Implementazione rotta ping `/rest/ping.view`
