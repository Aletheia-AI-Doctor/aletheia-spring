import { createSlice, type PayloadAction, nanoid } from '@reduxjs/toolkit';

export type NotificationType = 'success' | 'error' | 'warning' | 'info';

export interface Notification {
    id: string;
    type: NotificationType;
    title: string;
    message?: string;
}

interface NotificationState {
    notifications: Notification[];
}

const initialState: NotificationState = {
    notifications: [],
};

const notificationSlice = createSlice({
    name: 'notifications',
    initialState,
    reducers: {
        addNotification: {
            reducer: (state, action: PayloadAction<Notification>) => {
                state.notifications.push(action.payload);
            },
            prepare: (notification: Omit<Notification, 'id'>) => ({
                payload: { id: nanoid(), ...notification },
            }),
        },
        removeNotification: (state, action: PayloadAction<string>) => {
            state.notifications = state.notifications.filter(
                (notification) => notification.id !== action.payload
            );
        },
    },
});

export const { addNotification, removeNotification } = notificationSlice.actions;

// Helper actions for different notification types
export const sendSuccessNotification = (title: string, message?: string) =>
    addNotification({ type: 'success', title, message });

export const sendErrorNotification = (title: string, message?: string) =>
    addNotification({ type: 'error', title, message });

export const sendWarningNotification = (title: string, message?: string) =>
    addNotification({ type: 'warning', title, message });

export const sendInfoNotification = (title: string, message?: string) =>
    addNotification({ type: 'info', title, message });

export default notificationSlice.reducer;