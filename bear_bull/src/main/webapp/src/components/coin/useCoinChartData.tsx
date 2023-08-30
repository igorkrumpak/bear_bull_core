import { useState, useEffect, useCallback } from 'react';
import { IChart } from '../../rest/Interfaces';
import { fetchCoinChartData } from '../../rest/apiUtils';

export const useCoinChartData = (coin: string | null) => {
    const [chart, setChart] = useState<IChart>();

    const getDetails = useCallback(async (days: number) => {
        if (coin) {
            const chartData = await fetchCoinChartData(coin, days);
            setChart(chartData);
        }
    }, [coin]);

    useEffect(() => {
        getDetails(30);
    }, [getDetails]);

    return { chart, getDetails };
};