import React, { useRef } from 'react';
import { Chart as ChartJS, ChartType, registerables } from 'chart.js';
import { Chart as ReactChart } from 'react-chartjs-2';
import zoomPlugin from 'chartjs-plugin-zoom';
import annotationPlugin from 'chartjs-plugin-annotation';
import { IChart } from '../../rest/Interfaces';
import { existChartCategoryInSelectedCharts, existValueInMatrix, generateDefaultCharts } from './chartUtils';

interface ChartProps {
    chart: IChart;
    selectedCharts: string[][] | undefined;
    chartReference: React.RefObject<ChartJS<"line", number[], string>>;
}

export const Chart: React.FC<ChartProps> = ({ chart, selectedCharts, chartReference}) => {
    ChartJS.register(zoomPlugin);
    ChartJS.register(annotationPlugin);
    ChartJS.register(...registerables);

    const defaultCharts: string[][] = generateDefaultCharts(chart.xValues);
   
    let scales: { [key: string]: any } = {};
    let annotations: { [key: string]: any } = {};

    let existsPriceChart = existChartCategoryInSelectedCharts('PRICE_CHART', selectedCharts ? selectedCharts : defaultCharts, chart.xValues);
    let existsRsiMfiChart = existChartCategoryInSelectedCharts('OSCILLATOR_CHART', selectedCharts ? selectedCharts : defaultCharts, chart.xValues);
    let existsVolumeChart = existChartCategoryInSelectedCharts('VOLUME_CHART', selectedCharts ? selectedCharts : defaultCharts, chart.xValues);
    let existsPercentChart = existChartCategoryInSelectedCharts('PERCENT_CHART', selectedCharts ? selectedCharts : defaultCharts, chart.xValues);

    scales["x"] = {
        stacked: true,
        ticks: {
            beginAtZero: true
        },
        grid: {
            display: false
        }
    }

    if (existsPriceChart) {
        scales["Y_PRICE_CHART"] = {
            type: 'linear' as const,
            stack: 'demo' as const,
            stackWeight: 3,
            weight: 9,
            position: 'left' as const,
            offset: false,
            grid: {
                display: true
            }
        }
        if (existsRsiMfiChart || existsVolumeChart || existsPercentChart) {
            annotations["Y_PRICE_CHART"] = {
                type: 'line',
                borderColor: 'black',
                yMin: () => {
                    return chartReference.current?.scales['Y_PRICE_CHART'].min;
                },
                yMax: () => {
                    return chartReference.current?.scales['Y_PRICE_CHART'].min;
                },
                borderWidth: 1,
                display: () => {
                    return chartReference.current;
                },
                yScaleID: 'Y_PRICE_CHART',
            }
        }
    }
    if (existsRsiMfiChart) {
        scales["Y_OSCILLATOR_CHART"] = {
            type: 'linear' as const,
            stack: 'demo' as const,
            stackWeight: 1,
            weight: 8,
            position: 'left' as const,
            offset: true,
            grid: {
                display: true
            }
        }
        if (existsVolumeChart || existsPercentChart) {
            annotations["Y_OSCILLATOR_CHART"] = {
                type: 'line',
                borderColor: 'black',
                yMin: () => {
                    return chartReference.current?.scales['Y_OSCILLATOR_CHART'].min;
                },
                yMax: () => {
                    return chartReference.current?.scales['Y_OSCILLATOR_CHART'].min;
                },
                borderWidth: 1,
                display: () => {
                    return chartReference.current;
                },
                yScaleID: 'Y_OSCILLATOR_CHART',
            }
        }

    }
    if (existsVolumeChart) {
        scales["Y_VOLUME_CHART"] = {
            type: 'linear' as const,
            stack: 'demo' as const,
            stackWeight: 1,
            weight: 7,
            position: 'left' as const,
            offset: true,
            grid: {
                display: true
            }
        }
        if (existsPercentChart) {
            annotations["Y_VOLUME_CHART"] = {
                type: 'line',
                borderColor: 'black',
                yMin: () => {
                    return chartReference.current?.scales['Y_VOLUME_CHART'].min;
                },
                yMax: () => {
                    return chartReference.current?.scales['Y_VOLUME_CHART'].min;
                },
                borderWidth: 1,
                display: () => {
                    return chartReference.current;
                },
                yScaleID: 'Y_VOLUME_CHART',
            }
        }

    }
    if (existsPercentChart) {
        scales["Y_PERCENT_CHART"] = {
            type: 'linear' as const,
            stack: 'demo' as const,
            stackWeight: 1,
            weight: 6,
            position: 'left' as const,
            offset: true,
            grid: {
                display: true
            }
        }
    }

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        animation: {
            duration: 0
        },
        scales: scales,
        plugins: {
            tooltip: {
                intersect: false,
                mode: 'index' as const,
            },
            legend: {
                display: false
            },
            title: {
                display: true,
                text: chart.title,
            },
            zoom: {
                zoom: {
                    wheel: {
                    },
                    pinch: {

                    },
                    drag: {
                        enabled: true,
                    },
                    mode: 'x' as const
                }
            },
            annotation: {
                annotations: annotations
            }
        }
    };
    const labels = chart.yValues;
    const datasets = chart.xValues.filter(each => existValueInMatrix(each.label, selectedCharts ? selectedCharts : defaultCharts)).map(
        (each) => (
            {
                label: each.label,
                data: each.data,
                borderColor: each.color,
                backgroundColor: each.color,
                type: each.chartType as ChartType,
                tension: each.tension,
                yAxisID: 'Y_' + each.chartCategory,
                pointRadius: each.pointRadius
            }
        ));

    return (
        <div style={{ width: '100%', height: '500px' }}>
            <ReactChart
                ref={chartReference}
                options={options}
                data={{
                    datasets: datasets,
                    labels: labels,
                }}
                type="line"
            />
        </div>
    );
};
