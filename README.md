# HomeworkVTB12
1. Создайте таблицу items (id serial, val int, ...), добавьте в нее 40 строк со значением 0;
2. Запустите 8 параллельных потоков, в каждом из которых работает цикл, выбирающий
случайную строку в таблице и увеличивающий val этой строки на 1. Внутри транзакции
необходимо сделать Thread.sleep(5). Каждый поток должен сделать по 20.000 таких
изменений;
3. По завершению работы всех потоков проверить, что сумма всех val равна соответственно
160.000;
