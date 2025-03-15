import {MutationCache, QueryCache, QueryClient} from "@tanstack/react-query";
import {errorHandler} from "../hook/useCommon.ts";
import {HttpTypes} from "../utils/HttpTypes.ts";

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            refetchOnWindowFocus: false,
            retry: false,
        },
    },
    queryCache: new QueryCache({
        onError: (error) => {
            console.log("=>(QueryClient.tsx:13) error", error);
            errorHandler(error as HttpTypes);
        },
    }),
    mutationCache: new MutationCache({
        onError: (error) => {
            errorHandler(error as HttpTypes);
        }
    })
});

export default queryClient;
