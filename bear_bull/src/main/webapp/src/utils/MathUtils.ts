export class MathUtils {

    static formatPrice = (price: number): string => {
        if (price) {
            return price > 1 ? price.toFixed(2) : price.toPrecision(2);
        }
        return "";
        
    };


    static isValidNumber(str: unknown): boolean {
        const num = Number(str);
        return !isNaN(num);
    }
}