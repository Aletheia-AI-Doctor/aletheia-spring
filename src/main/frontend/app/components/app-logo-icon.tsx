import type {DetailedHTMLProps, ImgHTMLAttributes} from 'react';

export default function AppLogoIcon(props: DetailedHTMLProps<ImgHTMLAttributes<HTMLImageElement>, HTMLImageElement>) {
    return (
        <img
            {...props}
            alt="Alethia's Logo"
            src="https://tailwindcss.com/plus-assets/img/logos/mark.svg?color=indigo&shade=500"
        />
    );
}
