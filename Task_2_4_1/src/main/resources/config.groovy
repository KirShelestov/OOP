tasks {
    task {
        id = 'task1'
        name = 'Prime Numbers'
        maxPoints = 1
        softDeadline = '2025-05-01T23:59:59'
        hardDeadline = '2025-05-15T23:59:59'
    }
    
    task {
        id = 'task2'
        name = 'Pizzeria'
        maxPoints = 1
        softDeadline = '2025-05-15T23:59:59'
        hardDeadline = '2025-05-30T23:59:59'
    }
}

groups {
    group('22214') {
        student {
            githubUsername = 'student1'
            fullName = 'John Doe'
            repositoryUrl = 'https://github.com/student1/oop'
        }
        student {
            githubUsername = 'student2'
            fullName = 'Jane Smith'
            repositoryUrl = 'https://github.com/student2/oop'
        }
    }
}

checkpoints {
    checkpoint {
        name = 'Midterm'
        date = '2025-05-01T10:00:00'
    }
    checkpoint {
        name = 'Final'
        date = '2025-06-15T10:00:00'
    }
}

assignments {
    assign {
        student = 'student1'
        tasks = ['task1', 'task2']
    }
    assign {
        student = 'student2'
        tasks = ['task1']
    }
}

settings {
    testTimeout = 30 
    gradeScale = [
        'A': 90,
        'B': 80,
        'C': 70,
        'D': 60,
        'F': 0
    ]
}