export interface ApiResponse<T> {
    code: string;
    message: string;
    data: T | null;
}

export interface ApiSuccessResponse<T> extends ApiResponse<T> {
    code: string;
    message: string;
    data: T;
}

export interface ApiErrorResponse extends ApiResponse<null> {
    code: string;
    message: string;
    data: null;
}

export const isApiSuccess = <T>(response: ApiResponse<T>): response is ApiSuccessResponse<T> => {
    return response.data !== null;
};

export const isApiError = <T>(response: ApiResponse<T>): response is ApiErrorResponse => {
    return response.code !== "" && response.data === null;
};