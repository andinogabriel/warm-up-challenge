
/*
Migracion por defecto, se llama automaticamente cuando se inicia la applicacion
Hay que especificar este archivo en el application.properties con spring.datasource.initialization-mode=always
el ON CONFLICT (id) DO NOTHING; sirve para ver si ya existen estos datos en la DB no va a ejecutar el INSERT
*/


INSERT INTO categories (id, name) VALUES
    (1, 'Deporte'),
    (2, 'Ocio'),
    (3, 'Música'),
    (4, 'Arte'),
    (5, 'Noticia'),
    (6, 'Farandula')
    ON CONFLICT (id) DO NOTHING;