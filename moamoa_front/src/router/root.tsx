import { createBrowserRouter } from "react-router-dom";
import MainPage from "../pages/MainPage.tsx";
import BookmarkPage from "../pages/BookmarkPage.tsx";
import SummaryPage from "../pages/SummaryPage.tsx";
import RecordPage from "../pages/RecordPage.tsx";
import MobileLayout from "./MobileLayout.tsx";

const router = createBrowserRouter([
    {
        path: '/',
        element: <MobileLayout />,
        children: [
            {
                index: true,
                element: <MainPage />,
            },
            {
                path: 'bookmark',
                element: <BookmarkPage />,
            },
            {
                path: 'summary',
                element: <SummaryPage />,
            },
            {
                path: 'record',
                element: <RecordPage />,
            },
        ],
    },
]);

export default router;