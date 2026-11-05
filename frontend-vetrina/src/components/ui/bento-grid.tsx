"use client";

import { cn } from "@/lib/utils";
import { motion, useInView } from "motion/react";
import { useRef } from "react";

export const BentoGrid = ({
  className,
  children,
}: {
  className?: string;
  children?: React.ReactNode;
}) => {
  return (
    <div
      className={cn(
        "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 lg:gap-6",
        className
      )}
    >
      {children}
    </div>
  );
};

export const BentoGridItem = ({
  className,
  title,
  description,
  header,
  icon,
  index = 0,
}: {
  className?: string;
  title?: string | React.ReactNode;
  description?: string | React.ReactNode;
  header?: React.ReactNode;
  icon?: React.ReactNode;
  index?: number;
}) => {
  const ref = useRef<HTMLDivElement>(null);
  const isInView = useInView(ref, { once: true, margin: "-50px" });

  return (
    <motion.div
      ref={ref}
      initial={{ opacity: 0, y: 30, filter: "blur(6px)" }}
      animate={
        isInView
          ? { opacity: 1, y: 0, filter: "blur(0px)" }
          : { opacity: 0, y: 30, filter: "blur(6px)" }
      }
      transition={{
        duration: 0.6,
        delay: index * 0.1,
        ease: [0.21, 0.47, 0.32, 0.98],
      }}
      className={cn(
        "group relative overflow-hidden rounded-2xl border border-white/10",
        "bg-gradient-to-br from-dark-lighter/80 to-dark/60 backdrop-blur-sm",
        "p-6 lg:p-8 transition-all duration-500",
        "hover:border-primary/40 hover:shadow-2xl hover:shadow-primary/10",
        "hover:-translate-y-1",
        className
      )}
    >
      {/* Hover glow */}
      <div className="pointer-events-none absolute inset-0 opacity-0 group-hover:opacity-100 transition-opacity duration-500 bg-[radial-gradient(ellipse_at_50%_0%,rgba(44,95,45,0.15),transparent_70%)]" />

      {header && <div className="relative z-10 mb-6">{header}</div>}
      <div className="relative z-10">
        {icon && <div className="mb-4">{icon}</div>}
        {title && (
          <h3 className="text-xl font-bold text-white mb-3 group-hover:text-primary-light transition-colors duration-300">
            {title}
          </h3>
        )}
        {description && (
          <p className="text-white/50 leading-relaxed text-sm">{description}</p>
        )}
      </div>
    </motion.div>
  );
};
