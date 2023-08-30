export interface IDashboard {
    coinId: string;
    coinName: string;
    coinSymbol: string;
    coinThumbImage: string;
    reportDate: Date;
    price: number;
    totalVolume: number;
    marketCap: number;
    oscillatorsChart: number;
    bollingerBandsChart: number;
    movingAvaragesChart: number;
    tags: string;
    percentFromATH: number;
    percentFromYesterday: number;
    percentFromLastWeek: number;
    position: number;
}

export interface IChart {
    title: string;
    tags: string;
    yValues: string[];
    xValues: IChartData[];
}

export interface IChartData {
    label: string;
    color: string,
    chartType: string,
    chartCategory: string,
    tension: number,
    pointRadius: number,
    isChartEnabled: boolean,
    data: number[];
}

export interface CascadeOption {
    value: string | number;
    label: string;
    children?: CascadeOption[];
}