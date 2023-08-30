import { NavigateFunction } from 'react-router-dom';

export const navigateToCoinChart = (navigate: NavigateFunction, coinId: string) => {
    navigate({ pathname: "/coinChart", search: `?coinId=${coinId}` });
};