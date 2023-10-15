import axios from 'axios';
import { IChart, IDashboard } from './Interfaces';

export const fetchDashboardData = async (reportType?: string): Promise<IDashboard[]> => {
    const response = await axios(`/api/dashboard/${reportType}`);
    return response.data;
};

export const fetchCoinChartData = async (coin: string, days: number, reportType?: string): Promise<IChart> => {
    const response = await axios.get(`/api/chart/${reportType}/${coin}/${days}`);
    return response.data;
};
