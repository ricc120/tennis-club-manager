"use client";

import { useEffect, useRef } from "react";
import { cn } from "@/lib/utils";

export const BackgroundBeams = ({ className }: { className?: string }) => {
  const svgRef = useRef<SVGSVGElement>(null);

  return (
    <div className={cn("absolute inset-0 overflow-hidden", className)}>
      <svg
        ref={svgRef}
        className="absolute inset-0 h-full w-full"
        xmlns="http://www.w3.org/2000/svg"
      >
        <defs>
          <linearGradient id="beam-gradient-1" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="rgba(44, 95, 45, 0)" />
            <stop offset="50%" stopColor="rgba(44, 95, 45, 0.3)" />
            <stop offset="100%" stopColor="rgba(44, 95, 45, 0)" />
          </linearGradient>
          <linearGradient id="beam-gradient-2" x1="100%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stopColor="rgba(0, 229, 255, 0)" />
            <stop offset="50%" stopColor="rgba(0, 229, 255, 0.2)" />
            <stop offset="100%" stopColor="rgba(0, 229, 255, 0)" />
          </linearGradient>
          <linearGradient id="beam-gradient-3" x1="50%" y1="0%" x2="50%" y2="100%">
            <stop offset="0%" stopColor="rgba(255, 109, 0, 0)" />
            <stop offset="50%" stopColor="rgba(255, 109, 0, 0.15)" />
            <stop offset="100%" stopColor="rgba(255, 109, 0, 0)" />
          </linearGradient>
        </defs>
        {/* Animated beam lines */}
        <line
          x1="0"
          y1="0"
          x2="100%"
          y2="100%"
          stroke="url(#beam-gradient-1)"
          strokeWidth="1"
          className="animate-beam-1"
        />
        <line
          x1="100%"
          y1="0"
          x2="0"
          y2="100%"
          stroke="url(#beam-gradient-2)"
          strokeWidth="0.5"
          className="animate-beam-2"
        />
        <line
          x1="50%"
          y1="0"
          x2="50%"
          y2="100%"
          stroke="url(#beam-gradient-3)"
          strokeWidth="0.5"
          className="animate-beam-3"
        />
        <line
          x1="0"
          y1="50%"
          x2="100%"
          y2="30%"
          stroke="url(#beam-gradient-1)"
          strokeWidth="0.5"
          className="animate-beam-4"
        />
        <line
          x1="20%"
          y1="0"
          x2="80%"
          y2="100%"
          stroke="url(#beam-gradient-2)"
          strokeWidth="0.5"
          className="animate-beam-5"
        />
      </svg>
      {/* Radial gradient overlay */}
      <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_center,_rgba(44,95,45,0.08)_0%,_transparent_70%)]" />
    </div>
  );
};

export const FloatingParticles = ({ className }: { className?: string }) => {
  return (
    <div className={cn("absolute inset-0 overflow-hidden pointer-events-none", className)}>
      {Array.from({ length: 20 }).map((_, i) => (
        <div
          key={i}
          className="absolute rounded-full bg-primary/20 animate-float"
          style={{
            width: `${Math.random() * 4 + 2}px`,
            height: `${Math.random() * 4 + 2}px`,
            left: `${Math.random() * 100}%`,
            top: `${Math.random() * 100}%`,
            animationDelay: `${Math.random() * 5}s`,
            animationDuration: `${Math.random() * 10 + 10}s`,
          }}
        />
      ))}
    </div>
  );
};
