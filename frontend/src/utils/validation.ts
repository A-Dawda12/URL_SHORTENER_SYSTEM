const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;

export function validateEmail(email: string): string | undefined {
    const value = email.trim();
    if (!value) {
        return 'Email is required';
    }
    if (!EMAIL_REGEX.test(value)) {
        return 'Enter a valid email address';
    }
    return undefined;
} 

export function validatePassword(password: string): string | undefined {
    if(!password) {
        return 'Password is required';
    }
    if(password.length < 8) {
        return 'Password must be at least 8 characters';
    }
    if(!PASSWORD_REGEX.test(password)) {
        return 'Password must include uppercase, lowercase letters and a number';
    }
    return undefined;
}

export function validateDisplayName(displayName: string): string | undefined {
    const value = displayName.trim();
    if(!value) {
        return 'Display name is required';
    }
    if(value.length < 2 || value.length > 50) {
        return 'Display name must be between 2 and 50 characters';
    }
    return undefined;
}

export function validateConfirmPassword(password: string, confirmPassword: string): string | undefined {
    if(!confirmPassword) {
        return 'Please confirm your password';
    }
    if(password !== confirmPassword) {
        return 'Passwords do not match';
    }
    return undefined;
}