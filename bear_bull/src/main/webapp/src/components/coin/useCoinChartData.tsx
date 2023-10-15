import { useState, useEffect, useCallback } from 'react';
import { IChart } from '../../rest/Interfaces';
import { fetchCoinChartData } from '../../rest/apiUtils';
import { useGlobalState } from '../../state/GlobalStateProvider';

export const useCoinChartData = (coin: string | null) => {
    const [chart, setChart] = useState<IChart>();
    const { state } = useGlobalState();

    const getDetails = useCallback(async (days: number) => {
        if (coin) {
            const chartData = await fetchCoinChartData(coin, days, state.timeframe);
            setChart(chartData);
        }
    }, [coin, state]);

    useEffect(() => {
        getDetails(30);
    }, [getDetails]);

    return { chart, getDetails };
};