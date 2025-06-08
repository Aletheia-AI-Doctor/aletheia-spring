declare module 'trix' {
    export class TrixEditor extends HTMLElement {
        editor: any;
    }
}

declare global {
    namespace JSX {
        interface IntrinsicElements {
            'trix-editor': React.DetailedHTMLProps<
                React.HTMLAttributes<HTMLElement>,
                HTMLElement
            >;
        }
    }
}