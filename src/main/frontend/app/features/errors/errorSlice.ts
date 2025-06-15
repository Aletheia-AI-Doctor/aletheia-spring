import { createSlice, type PayloadAction } from '@reduxjs/toolkit'

type ErrorsState = Record<string, string[]>;

const initialState: ErrorsState = {};

export const errorsSlice = createSlice({
    name: 'globalErrors',
    initialState,
    reducers: {
        clearError: (state, action: PayloadAction<string>) => {
            delete state[action.payload];
        },
        clearAllErrors: (state) => ({}),
        setErrors: (state, action: PayloadAction<ErrorsState>) => {
            const errors = action.payload
            Object.keys(state).forEach(key => {
                delete state[key];
            });

            for (const [key, value] of Object.entries(errors)) {
                state[key] = value;
            }
        }
    },
})

export const { clearError, clearAllErrors, setErrors } = errorsSlice.actions

export default errorsSlice.reducer