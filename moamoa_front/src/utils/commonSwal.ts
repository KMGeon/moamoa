import { useCallback } from 'react';
import Swal from 'sweetalert2';
import {useQueryClient} from "@tanstack/react-query";

interface ISwalOptions {
    title: string;
    text?: string;
    icon: 'success' | 'error' | 'warning' | 'info' | 'question';
    confirmButtonText?: string;
    timer?: number;
    showConfirmButton?: boolean;
}

type ActionType = 'create' | 'update' | 'delete';

const commonSwal = () => {
    const queryClient = useQueryClient();
    const fireSwal = useCallback(
        async ({ title, text, icon, confirmButtonText = '확인', timer, showConfirmButton = true }: ISwalOptions) => {
            return await Swal.fire({
                title,
                text,
                icon,
                confirmButtonText,
                timer,
                showConfirmButton,
            });
        },
        []
    );

    const successSwal = useCallback((options: Partial<ISwalOptions> = {}) => {
        return fireSwal(
            {
                title: '성공',
                icon: 'success',
                ...options,
            }
        );
    }, [fireSwal]);

    const errorSwal = useCallback((options: Partial<ISwalOptions> = {}) => {
        return fireSwal(
            {
                title: '오류',
                icon: 'error',
                ...options,
            }
        );
    }, [fireSwal]);

    const actionSwal = useCallback(async (
        actionType: ActionType,
        queryKey?: string,
        options: Partial<ISwalOptions> = {}
    ) => {
        const actionMessages = {
            create: {
                title: '등록 성공',
                text: '성공적으로 등록되었습니다.'
            },
            update: {
                title: '수정 성공',
                text: '성공적으로 수정되었습니다.'
            },
            delete: {
                title: '삭제 성공',
                text: '성공적으로 삭제되었습니다.'
            }
        };

        const result_1 = await fireSwal({
            icon: 'success',
            ...actionMessages[actionType],
            ...options
        });
        if (queryKey && result_1.isConfirmed) {
            await queryClient.invalidateQueries({
                predicate: (query_1: any) => query_1.queryKey[0].toString().startsWith(queryKey)
            });
        }
        return result_1;
    }, [fireSwal, queryClient]);

    const reloadPage = () => {
        window.location.reload();
    };

    return { fireSwal, successSwal, errorSwal, actionSwal, reloadPage };
};

export default commonSwal;