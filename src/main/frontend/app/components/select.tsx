import React from "react";
import If from "~/components/if";

interface SelectProps {
    id: string;
    name?: string;
    required?: boolean;
    label?: string;
    value?: string;
    onChange?: (event: React.ChangeEvent<HTMLSelectElement>) => void;
    placeholder?: string;
    className?: string;
    dir?: string;
    defaultValue?: string;
    options: Array<{ value: string; label: string }>;
    disabled?: boolean;
}

const defaultProps: SelectProps = {
    id: 'input-' + Math.random().toString(36).substring(7),
    dir: 'auto',
    options: [],
}

export default function Select(props: SelectProps) {
    props = {...defaultProps, ...props};

    return (
        <div className={props.className}>
            <If condition={!! props.label}>
                <label htmlFor={props.id} className="block text-sm/6 font-medium text-gray-900">
                    {props.label} {props.required && <span>*</span>}
                </label>
            </If>
            <div className="mt-2">
                <select
                    disabled={props.disabled}
                    defaultValue={props.defaultValue}
                    id={props.id}
                    name={props.name}
                    dir={props.dir}
                    required={props.required}
                    value={props.value}
                    onChange={props.onChange}
                    className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-blue-600 sm:text-sm/6"
                >
                    (props.placeholder && <option value="">{props.placeholder}</option>)

                    {props.options.map(({label, value}) => (
                        <option key={value} value={value}>{label}</option>
                    ))}
                </select>
            </div>
        </div>
    );
}