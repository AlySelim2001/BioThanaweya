import React, { useEffect, useRef } from 'react';
import { Canvas } from '@react-three/fiber';
import * as THREE from 'three';
import { Points } from '@react-three/drei';

const particleCount = 800;

/** Silent error listener for WebGL context lost */
const useSafeWebGLListener = () => {
  useEffect(() => {
    const handler = (e: ErrorEvent) => {
      // WebGL context lost - silently swallow to prevent layout shift
    };
    ;(window as any).addEventListener('webglcontextlost', handler);
    return () => { (window as any).removeEventListener('webglcontextlost', handler); };
  }, []);
};

export const Global3DBackground = () => {
  useSafeWebGLListener();

  // Create neon DNA-style particle geometry
  useEffect(() => {
    const geometry = new THREE.BufferGeometry();
    const positions = new Float32Array(particleCount * 3);
    const colors = new Float32Array(particleCount * 3);
    const sizes = new Float32Array(particleCount);

    for (let i = 0; i < particleCount; i++) {
      // Create rotating DNA-inspired spiral structure
      const theta = (i / particleCount) * Math.PI * 2;
      const radius = 1 + (i / particleCount) * 2;
      const height = (Math.sin(i * 0.3) + 1) * 1.5;
      const colorMix = Math.cos(i * 0.2);

      // Neon Cyan: #06b6d4, Neon Green: #10b981
      const mix = (colorMix + 1) / 2; // 0 to 1
      const cyan = new THREE.Color(0x06b6d4);
      const emerald = new THREE.Color(0x10b981);
      const mixedColor = new THREE.Color().lerpColors([cyan, emerald], mix);

      positions[i * 3 + 0] = radius * Math.cos(theta) * Math.cos(i * 0.1);
      positions[i * 3 + 1] = height;
      positions[i * 3 + 2] = radius * Math.sin(theta) * Math.cos(i * 0.1);

      colors[i * 3 + 0] = mixedColor.r;
      colors[i * 3 + 1] = mixedColor.g;
      colors[i * 3 + 2] = mixedColor.b;

      sizes[i] = Math.random() * 0.3 + 0.1;
    }

    geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
    geometry.setAttribute('color', new THREE.BufferAttribute(colors, 3));
    geometry.setAttribute('size', new THREE.BufferAttribute(sizes, 1));
    ;(window as any)._bgGeometry = geometry;
  }, []);

  // Rotation animation state
  const rotationRef = useRef(0);

  return (
    <Canvas
      style={{
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100vw',
        height: '100vh',
        zIndex: -10,
        pointerEvents: 'none',
      }}
      dpr={[1, 1.5]}
      frameloop="demand"
      shadows={false}
      camera={{ fov: 75, near: 0.1, far: 100 }}
    >
      <Points pointSize={0.1}>
        <BufferGeometryAttributes
          geometry={geometry}
          position="position"
          color="color"
          size="size"
        />
        <PointMaterial
          size={0.3}
          transparent
          opacity={0.9}
          vertexColors
          blending={THREE.AdditiveBlending}
        />
      </Points>
    </Canvas>
  );
};