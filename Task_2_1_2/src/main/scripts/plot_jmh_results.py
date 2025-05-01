import pandas as pd
import matplotlib.pyplot as plt
import os

# Путь к текстовому файлу
results_path = os.path.abspath('build/results/jmh/results.txt')

# Проверка наличия файла
if not os.path.exists(results_path):
    print(f"Файл не найден: {results_path}")
    print("Убедитесь, что бенчмарки были успешно запущены.")
    exit(1)

# Чтение текстового файла
with open(results_path, 'r') as file:
    lines = file.readlines()

# Парсинг данных
data = []
for line in lines:
    if line.strip() and not line.startswith('Benchmark'):  # Пропускаем заголовок и пустые строки
        parts = line.split()
        benchmark = parts[0]
        mode = parts[1]
        cnt = int(parts[2])
        score = float(parts[3].replace(',', '.'))  # Заменяем запятую на точку
        error = float(parts[5].replace(',', '.'))  # Заменяем запятую на точку
        units = parts[6]
        data.append({
            'Benchmark': benchmark,
            'Mode': mode,
            'Cnt': cnt,
            'Score': score,
            'Error': error,
            'Units': units
        })

# Преобразование в DataFrame
results = pd.DataFrame(data)

# Фильтрация данных для каждого бенчмарка
sequential = results[results['Benchmark'].str.contains('sequential')]
parallel_stream = results[results['Benchmark'].str.contains('parallelStream')]
parallel_threads_2 = results[results['Benchmark'].str.contains('parallelThreads_2')]
parallel_threads_4 = results[results['Benchmark'].str.contains('parallelThreads_4')]
parallel_threads_8 = results[results['Benchmark'].str.contains('parallelThreads_8')]

# Построение графика
plt.figure(figsize=(10, 6))

# Добавление данных на график
plt.plot(sequential['Benchmark'], sequential['Score'], 'o-', label='Sequential')
plt.plot(parallel_stream['Benchmark'], parallel_stream['Score'], 's--', label='Parallel Stream')
plt.plot(parallel_threads_2['Benchmark'], parallel_threads_2['Score'], '^:', label='2 Threads')
plt.plot(parallel_threads_4['Benchmark'], parallel_threads_4['Score'], 'x-.', label='4 Threads')
plt.plot(parallel_threads_8['Benchmark'], parallel_threads_8['Score'], 'd-', label='8 Threads')

# Настройка графика
plt.xlabel('Benchmark')
plt.ylabel('Time (ms)')
plt.title('Prime Check Performance')
plt.legend()
plt.grid(True)
plt.yscale('log')  # Логарифмическая шкала для времени
plt.savefig('src/main/results/benchmark.png', dpi=300)  # Сохранение графика в файл
plt.show()  # Отображение графика