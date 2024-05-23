package application.cell;

import application.EnvironmentState;

public class CellBrain {

    public int[] inputData; // Входные нейроны - состояния квадратов + состояние клетки
    public float[][] weights; // Связи между входными нейронами и выходными
    public float[] outputData; // Выходные нейроны - действия клетки

    //todo Пока осознанно не учитывается клетка, на которой стоим - неясно, как её реализовать
    CellBrain(int visionAvailableSquareCount) {
        // todo сформируется циклическая зависимость: верхние клетки сказали что-то, нижние ответили, но к верхним информация не вернется.
        // todo более того, клетка, которая что-то сказала может стать уже мертвой (на следующий тик)
        // инициализируем массив входных нейронов
        inputData = new int[
                // количество возможных состояний квадратов * количество видимых квадратов
                EnvironmentState.SquareEnvironmentState.values().length * visionAvailableSquareCount
                        // количество хп
                        + 1
                ];
        // инициализируем веса между входными нейронами и выходными - здесь нужно проставить значения
//        weights = new float[
//                ][];
    }

}
