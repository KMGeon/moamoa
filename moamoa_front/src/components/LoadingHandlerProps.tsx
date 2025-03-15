import React, {type ReactNode} from 'react';
import {Fade, Box, Backdrop, CircularProgress} from '@mui/material';

interface LoadingHandlerProps {
    isLoading: boolean;
    fallback: ReactNode;
    children: ReactNode;
    timeout?: number;
    isPending?: boolean;
}


const LoadingHandler: React.FC<LoadingHandlerProps> = ({
                                                           children,
                                                           isLoading,
                                                           isPending = false,
                                                           fallback = <CircularProgress/>,
                                                           timeout = 300
                                                       }) => {
    const content = (
        <Box position="relative">
            {!isLoading && (
                <Fade in={!isLoading} timeout={timeout}>
                    <Box>{children}</Box>
                </Fade>
            )}
            <Fade in={isLoading} timeout={timeout}>
                <Box
                    position="absolute"
                    top={0}
                    left={0}
                    right={0}
                    bottom={0}
                    display={isLoading ? 'flex' : 'none'}
                    alignItems="center"
                    justifyContent="center"
                >
                    {fallback}
                </Box>
            </Fade>
        </Box>
    );

    return (
        <>
            <Box position="relative">
                {content}
            </Box>
            {isPending && (
                <Backdrop
                    sx={{
                        color: '#fff',
                        zIndex: (theme) => theme.zIndex.drawer + 1,
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%'
                    }}
                    open={isPending}
                >
                    {fallback}
                </Backdrop>
            )}
        </>
    );
};

export default LoadingHandler;