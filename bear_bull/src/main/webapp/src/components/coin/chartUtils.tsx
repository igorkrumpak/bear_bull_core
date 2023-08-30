import { IChartData } from "../../rest/Interfaces";

export const existValueInMatrix = (value: string, matrix: string[][]): boolean => {
    if (!matrix) return false;
    for (let i = 0; i < matrix.length; i++) {
        for (let j = 0; j < matrix[i].length; j++) {
            if (matrix[i][j].includes(value)) {
                return true;
            }
        }
    }
    return false;
};

export const existChartCategoryInSelectedCharts = (
    value: string,
    matrix: string[][],
    chartValues: any[]
): boolean => {
    if (!matrix) return false;
    for (let i = 0; i < matrix.length; i++) {
        for (let j = 0; j < matrix[i].length; j++) {
            let chartName = matrix[i][j];
            let chartData = chartValues.find((each) => each.label.includes(chartName));
            if (chartData?.chartCategory.includes(value)) {
                return true;
            }
        }
    }
    return false;
};

export const generateDefaultCharts = ( chartData : IChartData[] ) : string[][] => {
    return chartData.filter(each => each.isChartEnabled).map<string[]>(each => [each.label]);
}