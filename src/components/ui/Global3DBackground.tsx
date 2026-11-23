import React, { useEffect } from 'react';
import { Canvas } from '@react-three/fiber';
import * as THREE from 'three';
import { Points, PointMaterial } from '@react-three/drei';

const particleCount = 500;

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

  // Create particle geometry and material effect
  useEffect(() => {
    const geometry = new THREE.BufferGeometry();
    const positions = new Float32Array(particleCount * 3);
    const sizes = new Float32Array(particleCount);

    for (let i = 0; i < particleCount; i++) {
      positions[i * 3 + 0] = (Math.random() - 0.5) * 4;
      positions[i * 3 + 1] = (Math.random() - 0.5) * 4;
      positions[i * 3 + 2] = (Math.random() - 0.5) * 4;
      sizes[i] = Math.random() * 0.1 + 0.05;
    }

    geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
    geometry.setAttribute('size', new THREE.BufferAttribute(sizes, 1));

    // Store for use in render
    ;(window as any)._bgGeometry = geometry;
  }, []);

  return (
    <Canvas
      style={{
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100vw',
        height: '100vh',
        zIndex: -1,
        pointerEvents: 'none',
      }}
      dpr={[1, 1.5]}
      frameloop="demand"
      shadows={false}
      camera={{ fov: 45, near: 0.1, far: 100 }}
    >
      <Points>
        <PointMaterial
          size={0.05}
          transparent
          opacity={0.6}
          color={0x64ffda}
          blending={THREE.AdditiveBlending}
          fog={false}
        />
      </Points>
    </Canvas>
  );
};