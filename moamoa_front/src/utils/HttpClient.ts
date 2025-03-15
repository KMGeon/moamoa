import axios, {AxiosInstance} from 'axios';
import {singleton} from "tsyringe";
import {HttpRequestConfig, HttpTypes} from './HttpTypes.ts';

@singleton()
export class HttpClient {
    private readonly axiosInstance: AxiosInstance;

    constructor() {
        this.axiosInstance = axios.create({
            // baseURL: import.meta.env.VITE_SERVER_URL + '/api',
            baseURL: 'http://localhost:8080',
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
            },
            timeout: 10000, // 10초 타임아웃 설정
        });

        // 기본 에러 핸들링 설정
        this.setupErrorHandler();
    }

    private setupErrorHandler(): void {
        this.axiosInstance.interceptors.response.use(
            (response) => response,
            (error) => {
                const { response } = error;
                const httpError = new HttpTypes(
                    response?.status,
                    response?.data?.message || error.message,
                    response?.data?.validation
                );
                return Promise.reject(httpError);
            }
        );
    }

    private createParams(
        params?: Record<string, unknown> | string
    ): Record<string, unknown> | string | undefined {
        if (!params) return undefined;
        if (typeof params === 'string') return params;
        
        return Object.entries(params).reduce((acc, [key, value]) => {
            if (value != null) {
                acc[key] = value;
            }
            return acc;
        }, {} as Record<string, unknown>);
    }

    async request<T>(config: HttpRequestConfig): Promise<T> {
        const { method = 'GET', url, params, data, headers = {} } = config;
        
        try {
            const response = await this.axiosInstance.request<T>({
                method,
                url,
                params: this.createParams(params),
                data,
                headers,
            });
            
            return response.data;
        } catch (error) {
            if (error instanceof HttpTypes) {
                throw error;
            }
            throw new HttpTypes(500, '서버 오류가 발생했습니다.');
        }
    }

    // HTTP 메서드별 래퍼 메서드
    async getData<T>(url: string, params?: Record<string, unknown> | string): Promise<T> {
        return this.request<T>({ method: 'GET', url, params });
    }

    async postData<T>(url: string, data: unknown, params?: Record<string, unknown>): Promise<T> {
        return this.request<T>({ method: 'POST', url, data, params });
    }

    async patchData<T>(url: string, data: unknown, params?: Record<string, unknown>): Promise<T> {
        return this.request<T>({ method: 'PATCH', url, data, params });
    }

    async deleteData<T>(url: string, data?: unknown, params?: Record<string, unknown>): Promise<T> {
        return this.request<T>({ method: 'DELETE', url, data, params });
    }

    async getTypeData<T = any>(url: string, params?: Record<string, string | number | undefined>): Promise<T> {
        return this.request<T>({ method: 'GET', url, params });
    }
}