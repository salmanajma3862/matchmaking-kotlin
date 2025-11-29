import { ImageWithFallback } from './figma/ImageWithFallback';

interface MatchCardProps {
  id: string;
  name: string;
  image: string;
  isNew?: boolean;
}

export function MatchCard({ name, image, isNew }: MatchCardProps) {
  return (
    <div className="flex flex-col items-center gap-2 min-w-[80px]">
      <div className="relative">
        <div className="w-16 h-16 rounded-full overflow-hidden ring-2 ring-pink-500">
          <ImageWithFallback
            src={image}
            alt={name}
            className="w-full h-full object-cover"
          />
        </div>
        {isNew && (
          <div className="absolute -top-1 -right-1 w-5 h-5 bg-pink-500 rounded-full flex items-center justify-center">
            <span className="text-white text-xs">★</span>
          </div>
        )}
      </div>
      <span className="text-xs text-gray-700 truncate w-full text-center">{name}</span>
    </div>
  );
}
