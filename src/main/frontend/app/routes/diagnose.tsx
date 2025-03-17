export default function () {
    const range = [];
    for (let i = 0; i < 100; i++) {
        range.push(i);
    }

    return (
        <div>
            <h1 className="text-xl font-semibold">Diagnose</h1>
            <p>Diagnose image (use filepond for file upload, enjoy)</p>

            {/* This is how you create loops inside the html */}
            <div className="grid grid-cols-2 w-full pt-5 gap-4">
                {range.map((i) => (
                    <div key={i}>{i + 1}</div>
                ))}
            </div>
        </div>
    );
}