# Package `config`

Configurazione trasversale dell'applicazione web (non legata alla sicurezza).

| File | Ruolo |
|---|---|
| `WebConfig` | `@Configuration` che implementa `WebMvcConfigurer`. In `addCorsMappings` apre il CORS su **tutti i path** (`/**`) per l'origine `http://localhost:4200`, metodi `GET, POST, PUT, DELETE`, tutti gli header. Serve a far accettare al browser le chiamate del frontend Angular in sviluppo. |

## Nota: doppia configurazione CORS

Il CORS è definito in **due punti**:
1. qui, `WebConfig` (a livello Spring MVC);
2. in `security/SecurityConfiguration` → bean `corsConfigurationSource()` (a livello Spring Security, con anche `OPTIONS` e `allowCredentials`).

Quando Spring Security è attivo è quest'ultima ad avere effetto sulle richieste che
passano dalla filter chain. `WebConfig` resta come configurazione MVC di base.
Per evitare ambiguità, in un intervento futuro conviene tenerne una sola.
