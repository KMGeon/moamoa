import "reflect-metadata";
import React from "react";
import * as ReactDOM from 'react-dom/client';
import {QueryClientProvider} from "@tanstack/react-query";
import {ReactQueryDevtools} from "@tanstack/react-query-devtools";
import queryClient from "./config/QueryClient.tsx";
import App from "./App";

const root = ReactDOM.createRoot(
    document.getElementById("root") as HTMLElement
);

root.render(
    <React.StrictMode>
        <QueryClientProvider client={queryClient}>
            <App />
            <ReactQueryDevtools initialIsOpen={true} />
        </QueryClientProvider>
    </React.StrictMode>
);