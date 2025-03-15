import {useTest1} from "../hook/api/useTest.ts";

export default function MainPage() {
    // const {data} = useTest1();
    const {data: test1} = useTest1();


    console.log("=>(MainPage.tsx:12) test1", test1);


    return (
        <div>hello world</div>
    );
}