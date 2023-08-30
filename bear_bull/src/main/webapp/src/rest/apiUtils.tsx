import axios from 'axios';
import { IChart, IDashboard } from './Interfaces';

export const fetchDashboardData = async (): Promise<IDashboard[]> => {
    const response = await axios('/api/dashboard');
    return response.data;
};

export const fetchCoinChartData = async (coin: string, days: number): Promise<IChart> => {
    const response = await axios.get(`/api/chart/${coin}/${days}`);
    return response.data;
};
