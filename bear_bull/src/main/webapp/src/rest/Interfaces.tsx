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
    percentFromLastPeriod: number;
    position: number;
    rsi: number,
    rsiChartColor: string,
    rsiLabel: string,
    mfi: number,
	mfiChartColor: string,
	mfiLabel: string,
	stochasticOscillator: number,
	stochasticOscillatorChartColor: string,
	stochasticOscillatorLabel: string,
	priceChartColor: string,
	priceLabel: string,
	upperBand: number,
	upperBandChartColor: string,
	upperBandLabel: string,
	lowerBand: number,
	lowerBandChartColor: string,
	lowerBandLabel: string,
	avgPrice20Periods: number,
	avgPrice20PeriodsChartColor: string,
	avgPrice20PeriodsLabel: string,
	avgPrice50Periods: number,
	avgPrice50PeriodsChartColor: string,
	avgPrice50PeriodsLabel: string,
	avgPrice200Periods: number,
	avgPrice200PeriodsChartColor: string,
	avgPrice200PeriodsLabel: string,
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

export interface TooltipDashboardChartValues {
    color: string;
    label: string;
    value: number
}