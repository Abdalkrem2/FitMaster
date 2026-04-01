import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { LogIn } from "lucide-react";
import { Card, CardHeader, CardTitle } from "../components/ui/Card";
import { Input } from "../components/ui/Input";
import { Button } from "../components/ui/Button";
import { authService } from "../services/authService";
import { useAuth } from "../context/AuthContext";

const Login: React.FC = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth(); //form context

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username || !password) {
      setError("Please enter both username and password.");
      return;
    }

    try {
      setLoading(true);
      setError("");
      const { token, user } = await authService.login(username, password);

      // Check if user is activated (only for employees)
      if (user.roles.includes("EMPLOYEE") && !user.isActivated) {
        setError(
          "Your account has been deactivated. Please contact the administrator.",
        );
        return;
      }

      login(token, user); //to stor token && user & roles

      if (user.roles.includes("EMPLOYEE")) {
        navigate("/e-dashboard"); // Employee Dashboard
      } else if (user.roles.includes("ADMIN")) {
        navigate("/"); //Admin Dashboard
      } else if (user.roles.includes("MEMBER")) {
        navigate("/member-dashboard"); //Member Dashboard
      }
    } catch (err: any) {
      setError("Invalid username or password.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
      <div className="w-full max-w-md animate-in fade-in slide-in-from-bottom-4 duration-500">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-extrabold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
            FitMaster
          </h1>
          <p className="mt-2 text-sm text-gray-600">
            Sign in to manage your gym
          </p>
        </div>

        <Card padding="lg" className="w-full">
          <CardHeader>
            <CardTitle>Login</CardTitle>
          </CardHeader>
          <form onSubmit={handleLogin} className="space-y-4">
            <Input
              label="Username or Phone"
              type="text"
              placeholder="Enter your username or phone"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              error={error ? " " : undefined} // trigger red border without duplicated message
            />
            <Input
              label="Password"
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            {error && (
              <div className="text-sm text-red-600 bg-red-50 p-3 rounded-lg">
                {error}
              </div>
            )}

            <Button type="submit" fullWidth disabled={loading} className="mt-6">
              {loading ? (
                "Signing in..."
              ) : (
                <>
                  <LogIn className="w-4 h-4 mr-2" />
                  Sign in
                </>
              )}
            </Button>
          </form>
        </Card>
      </div>
    </div>
  );
};

export default Login;
