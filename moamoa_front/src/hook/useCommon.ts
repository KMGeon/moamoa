import {UseMutationOptions, UseMutationResult, useQuery, UseQueryResult} from "@tanstack/react-query";
import {container} from "tsyringe";
import {HttpTypes} from "../utils/HttpTypes.ts";
import Swal from "sweetalert2";


export type DEFAULT_MUTATE_TYPE = UseMutationResult<any, Error, any, unknown>;
export type ServiceQueryType<T> = UseQueryResult<T, Error>;

export function useRepositoryQuery<T, R>(
    RepositoryClass: { new(...args: any[]): T },
    methodName: keyof T & string,
    queryKey: (string | Record<string, unknown> | undefined)[],
    params?: Record<string, unknown> | undefined,
    options?: UseMutationOptions<void, unknown, Record<string, unknown>, unknown>,
    isParamObject: boolean = false
): UseQueryResult<R, Error> {
    const repository = container.resolve(RepositoryClass);

    const queryFn = () => {
        if (isParamObject) {
            const urlParams = new URLSearchParams();
            if (params) {
                Object.entries(params).forEach(([key, value]) => {
                    if (value !== undefined && value !== null) {
                        urlParams.append(key, String(value));
                    }
                });
            }
            return (repository[methodName] as (params?: string) => Promise<R>)(urlParams.toString());
        } else {
            return (repository[methodName] as (params?: Record<string, unknown>) => Promise<R>)(params);
        }
    };

    return useQuery<R, Error>({
        queryKey,
        queryFn,
        ...options
    });
}



export const errorHandler = (error: HttpTypes) => {
    console.error(error);

    if (error.httpStatus !== 401) {
        let errorMessage = `<p>${error.message}</p>`;

        if (error.validation) {
            const validationMessages = Object.entries(error.validation)
                .map(([field, messages]) => `<strong>${field}:</strong> ${messages.join(', ')}`)
                .join("<br>");
            errorMessage += `<hr><p>${validationMessages}</p>`;

            Swal.fire({
                title: "유효성 검사 실패",
                html: errorMessage,
                icon: "error",
                confirmButtonText: "OK"
            });
        } else {
            Swal.fire({
                title: "Error",
                html: errorMessage,
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    }
}
